package com.ebcf.jnab.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

// Clase que extiende MutableLiveData para emitir eventos que se consumen una sola vez.
// Util para notificar acciones como navegacion, mensajes, o comandos que no deben repetirse
// cuando se rota la pantalla o se reanudan los observadores.
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    // Permite llamar sin datos, por ejemplo: SingleLiveEvent<Unit>
    fun call() {
        value = null
    }

}