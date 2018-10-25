package cc.narztiizzer.brief.merging

import cc.narztiizzer.brief.merging.annotation.Mergable
import cc.narztiizzer.brief.merging.annotation.Unique
import cc.narztiizzer.brief.merging.exception.MergeException
import cc.narztiizzer.brief.merging.interfaces.InstanceMerger

class Merger<T>(private var kClass: Class<T>) {

    private var mInstanceMerger: InstanceMerger<T>? = null

    @Suppress("REDUNDANT_LABEL_WARNING", "NOT_A_FUNCTION_LABEL_WARNING")
    fun mergeBetween(list1: ArrayList<T>, list2: ArrayList<T>): ArrayList<T>{
        val tempResult = ArrayList<T>()
        val tempSelf = list1
        val tempTarget = list2
        val rawType = this.kClass

        rawType.getAnnotation(Mergable::class.java) ?: throw MergeException("Instance not support to merge.")

        val unique = rawType.declaredFields.find {
            it.getAnnotation(Unique::class.java) != null
        } ?: throw MergeException("Instance not found unique key.")

        if(!unique.type.isPrimitive && !unique.type.isAssignableFrom(String::class.java))
            throw MergeException("Merge key should be primitive type or string.")

        if(mInstanceMerger == null) {
            mInstanceMerger = this.defaultInstanceMerger()
            println("Instance merger not define, result after merge maybe not correct.")
        }

        selfLoop@
        tempSelf.forEach { _selfItem ->
            val selfItemIns = rawType.cast(_selfItem)
            val selfItemField = rawType.getDeclaredField(unique.name)
            val selfItemFieldAccessible = selfItemField.isAccessible

            selfItemField.isAccessible = true
            val selfItemResult = selfItemField.get(selfItemIns).toString()
            selfItemField.isAccessible = selfItemFieldAccessible

            tempTarget.forEachIndexed { index, _targetItem ->
                val targetItemIns = rawType.cast(_targetItem)
                val targetItemField = rawType.getDeclaredField(unique.name)
                val targetItemFieldAccessible = targetItemField.isAccessible

                targetItemField.isAccessible = true
                val targetItemResult = targetItemField.get(targetItemIns).toString()
                targetItemField.isAccessible = targetItemFieldAccessible

                if(selfItemResult == targetItemResult){

                    val mergeResult = this.mInstanceMerger!!.onMergeInstance(_selfItem, _targetItem)
                    tempTarget.removeAt(index)
                    mergeResult?.let { tempResult.add(it) }

                    return@forEach
                }
            }

            val mergeResult = this.mInstanceMerger!!.onMergeInstance(_selfItem, null)
            mergeResult?.let { tempResult.add(it) }
        }

        if(tempTarget.size > 0){
            tempTarget.forEach { _targetItem ->
                val mergeResult = this.mInstanceMerger!!.onMergeInstance(null, _targetItem)
                mergeResult?.let { tempResult.add(it) }
            }
        }

        return tempResult
    }

    internal fun setInstanceMerger(merger: InstanceMerger<T>): Merger<T> {
        this.mInstanceMerger = merger
        return this
    }

    private fun defaultInstanceMerger(): InstanceMerger<T>{
        return object: InstanceMerger<T>{
            override fun onMergeInstance(item1: T?, item2: T?): T? {
                return if ((item1 != null && item2 != null) || item1 != null)
                    item1
                else
                    item2
            }
        }
    }
}
