package com.example.jbox2dplaygroundapp

import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.contacts.Contact

class ContactListenerManager(private val onCollision: (Body, Body) -> Unit) :
    ContactListener {
    override fun beginContact(contact: Contact?) {
        contact?.let {
            val bodyA = contact.fixtureA.body
            val bodyB = contact.fixtureB.body

            onCollision(bodyA, bodyB)
        }
    }

    override fun endContact(contact: Contact?) {
        // no-op
    }

    override fun preSolve(
        contact: Contact?,
        oldManifold: Manifold?
    ) {
        // no-op
    }

    override fun postSolve(
        contact: Contact?,
        impulse: ContactImpulse?
    ) {
        // no-op
    }
}