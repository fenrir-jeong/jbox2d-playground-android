package com.example.jbox2dplaygroundapp

import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World

data class RectBody(val x: Float, val y: Float, val w: Float, val h: Float)

class PhysicsWorld {
    private val gravity = Vec2(0f, 9.8f)
    private val world = World(gravity)
    private val scale = 50f
    private val destructionQueue = mutableListOf<Body>()
    private val spawnQueue = mutableListOf<() -> Unit>()
    val particles = mutableListOf<Particle>()

    init {
        world.setContactListener(ContactListener { a, b ->
            val (particleBody, wallBody) = when {
                a.userData == "breakable" && b.userData == "wall" -> a to b
                b.userData == "breakable" && a.userData == "wall" -> b to a
                else -> return@ContactListener
            }

            if (destructionQueue.contains(particleBody)) return@ContactListener

            destructionQueue.add(particleBody)

            val position = particleBody.position
            val radius = 0.5f
            val count = 200

            repeat(count) {
                val dx = (-1..1).random().toFloat()
                val dy = (-1..1).random().toFloat()
                spawnQueue.add { createParticle(position.x + dx, position.y + dy, radius, userData = "fragment") }
            }
        })
    }

    fun createWorldBounds(screenWidthPx: Float, screenHeightPx: Float) {
        val w = screenWidthPx / scale
        val h = screenHeightPx / scale

        val thickness = 1f

        val bounds = listOf(
            RectBody(x = w / 2, y = h + thickness / 2, w = w, h = thickness),
            RectBody(x = w / 2, y = -thickness / 2, w = w, h = thickness),
            RectBody(x = -thickness, y = h / 2, w = thickness, h = h),
            RectBody(x = w + thickness, y = h / 2, w = thickness, h = h)
        )

        for (bound in bounds) {
            val bodyDef = BodyDef().apply {
                type = BodyType.STATIC
                position.set(bound.x, bound.y)
            }
            val body = world.createBody(bodyDef)

            val shape = PolygonShape().apply {
                setAsBox(bound.w / 2, bound.h / 2)
            }

            val fixtureDef = FixtureDef().apply {
                this.shape = shape
                restitution = 0.6f
                friction = 0.3f
            }

            body.createFixture(fixtureDef)
            body.userData = "wall"
        }
    }

    fun initialParticle() {
        createParticle(10f, 10f, 1.0f).body.apply {
            linearVelocity = Vec2(2f, 2f)
            userData = "breakable"
        }
    }

    fun createParticle(x: Float, y: Float, radius: Float = 0.2f, userData: String = "particle"): Particle {
        val bodyDef = BodyDef().apply {
            type = BodyType.DYNAMIC
            position.set(x, y)
        }
        val body = world.createBody(bodyDef)

        val shape = CircleShape().apply {
            m_radius = radius
        }

        val fixtureDef = FixtureDef().apply {
            this.shape = shape
            density = 0.1f
            restitution = 0.8f
            friction = 0.1f
        }

        body.createFixture(fixtureDef)
        body.userData = userData

        val particle = Particle(body, radius)
        particles.add(particle)
        return particle
    }

    fun applyForceToAll(fx: Float, fy: Float) {
        var b = world.bodyList
        while (b != null) {
            b.applyForceToCenter(Vec2(fx, fy))
            b = b.next
        }
    }

    fun step() {
        world.step(1 / 60f, 6, 2)

        destructionQueue.forEach { body ->
            world.destroyBody(body)
            particles.removeAll { it.body == body }
        }
        destructionQueue.clear()


//        particles.addAll(spawnQueue)
        spawnQueue.forEach { it.invoke() }
        spawnQueue.clear()
    }
}