package cc.narztiizzer.brief.merging.interfaces

interface InstanceMerger<T> {
    fun onMergeInstance(item1: T?, item2: T?): T?
}