package you.thiago.qualanota.data

object Database {

    private lateinit var instance: AppDatabase

    fun get() = instance

    fun set(instance: AppDatabase) {
        this.instance = instance
    }
}
