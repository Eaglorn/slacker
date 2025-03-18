package ru.fku.slacker.utils

class SwissTable<K, V>(initialCapacity : Int = 16) {
    private var size : Int = 0
    private var capacity : Int = initialCapacity
    private var keys : Array<Any?> = arrayOfNulls(capacity)
    private var values : Array<Any?> = arrayOfNulls(capacity)
    private var control : ByteArray = ByteArray(capacity)

    private val EMPTY : Byte = 0
    private val DELETED : Byte = - 1
    private val OCCUPIED : Byte = 1

    private fun hash(key : K) : Int {
        return key.hashCode() and 0x7FFFFFFF
    }

    private fun probe(index : Int, attempt : Int) : Int {
        return (index + attempt * attempt) % capacity
    }

    private fun findSlot(key : K) : Int {
        val hash = hash(key)
        var attempt = 0
        var index = hash % capacity
        while (control[index] != EMPTY) {
            if (control[index] == OCCUPIED && keys[index] == key) {
                return index
            }
            attempt ++
            index = probe(hash % capacity, attempt)
        }
        return - 1
    }

    fun put(key : K, value : V) {
        if (size >= capacity * 0.75) {
            resize()
        }

        val hash = hash(key)
        var attempt = 0
        var index = hash % capacity
        while (control[index] == OCCUPIED) {
            if (keys[index] == key) {
                values[index] = value
                return
            }
            attempt ++
            index = probe(hash % capacity, attempt)
        }

        keys[index] = key
        values[index] = value
        control[index] = OCCUPIED
        size ++
    }

    operator fun get(key : K) : V? {
        val index = findSlot(key)
        return if (index != - 1) values[index] as V else null
    }

    operator fun set(key : K, value : V) {
        put(key, value)
    }

    fun remove(key : K) : Boolean {
        val index = findSlot(key)
        if (index == - 1) return false

        keys[index] = null
        values[index] = null
        control[index] = DELETED
        size --
        return true
    }

    private fun resize() {
        val oldKeys = keys
        val oldValues = values
        val oldControl = control

        capacity *= 2
        keys = arrayOfNulls(capacity)
        values = arrayOfNulls(capacity)
        control = ByteArray(capacity)
        size = 0

        for (i in oldKeys.indices) {
            if (oldControl[i] == OCCUPIED) {
                put(oldKeys[i] as K, oldValues[i] as V)
            }
        }
    }

    fun filter(predicate : (Pair<K, V>) -> Boolean) : SwissTable<K, V> {
        val result = SwissTable<K, V>(this.capacity)
        for (i in keys.indices) {
            if (control[i] == OCCUPIED) {
                val key = keys[i] as K
                val value = values[i] as V
                val pair = Pair(key, value)
                if (predicate(pair)) {
                    result.put(key, value)
                }
            }
        }
        return result
    }

    fun forEach(action : (Pair<K, V>) -> Unit) {
        for (i in keys.indices) {
            if (control[i] == OCCUPIED) {
                val key = keys[i] as K
                val value = values[i] as V
                action(Pair(key, value))
            }
        }
    }

    override fun toString() : String {
        val sb = StringBuilder()
        for (i in keys.indices) {
            if (control[i] == OCCUPIED) {
                sb.append("${keys[i]}=${values[i]}, ")
            }
        }
        return sb.toString()
    }
}