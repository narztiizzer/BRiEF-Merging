package cc.narztiizzer.brief.merging

import cc.narztiizzer.brief.merging.interfaces.InstanceMerger
import cc.narztiizzer.brief.merging.model.MergeModel
import org.junit.Assert.*
import org.junit.Test

class MergingTest {
    @Suppress("USELESS_IS_CHECK")
    @Test
    fun builderTest() {
        val mergerBuilder = MergerBuilder(MergeModel::class.java)
        assert(mergerBuilder is MergerBuilder<MergeModel>)
    }

    @Test
    fun mergeTest(){
        val list1 = ArrayList<MergeModel>()
        val list2 = ArrayList<MergeModel>()

        val mergeModel1 = MergeModel()
        val mergeModel2 = MergeModel()
        val mergeModel3 = MergeModel()
        val mergeModel4 = MergeModel()
        val mergeModel5 = MergeModel()

        mergeModel1.uniqueID = 1
        mergeModel2.uniqueID = 2
        mergeModel2.message = "List 1"
        mergeModel3.uniqueID = 3
        mergeModel4.uniqueID = 4
        mergeModel5.uniqueID = 2
        mergeModel5.message = "List 2"

        list1.add(mergeModel1)
        list1.add(mergeModel2)

        list2.add(mergeModel3)
        list2.add(mergeModel4)
        list2.add(mergeModel5)

        val result = Merger(MergeModel::class.java).mergeBetween(list1, list2)

        assertEquals(4, result.size)

    }

    @Test
    fun mergerBuilderTest(){
        val list1 = ArrayList<MergeModel>()
        val list2 = ArrayList<MergeModel>()

        val mergeModel1 = MergeModel()
        val mergeModel2 = MergeModel()
        val mergeModel3 = MergeModel()
        val mergeModel4 = MergeModel()
        val mergeModel5 = MergeModel()

        mergeModel1.uniqueID = 1
        mergeModel2.uniqueID = 2
        mergeModel2.message = "List 1"
        mergeModel3.uniqueID = 3
        mergeModel4.uniqueID = 4
        mergeModel5.uniqueID = 2
        mergeModel5.message = "Sample list 2"

        list1.add(mergeModel1)
        list1.add(mergeModel2)

        list2.add(mergeModel3)
        list2.add(mergeModel4)
        list2.add(mergeModel5)

        val builder = MergerBuilder(MergeModel::class.java).
            setInstanceMerger(object : InstanceMerger<MergeModel>{
                override fun onMergeInstance(item1: MergeModel?, item2: MergeModel?): MergeModel? {
                    return  if ((item1 != null && item2 != null)) {
                                if (item2.message.length >= item1.message.length)
                                    item2
                                else
                                    item1
                            } else item1 ?: item2
                }
            })

        val result = builder.create().mergeBetween(list1, list2)

        assertEquals(4, result.size)

    }
}