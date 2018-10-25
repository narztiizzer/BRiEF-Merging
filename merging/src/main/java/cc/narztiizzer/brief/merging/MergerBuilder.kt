package cc.narztiizzer.brief.merging

import cc.narztiizzer.brief.merging.interfaces.InstanceMerger

class MergerBuilder<T>(){

    private var instanceMerger: InstanceMerger<T>? = null
    private lateinit var kClass: Class<T>

    constructor(kClass: Class<T>): this() { this.kClass = kClass }

    fun setInstanceMerger(merger: InstanceMerger<T>): MergerBuilder<T> {
        this.instanceMerger = merger
        return this
    }

    fun create(): Merger<T>{
        return Merger(kClass).setInstanceMerger(this.instanceMerger!!)
    }
}