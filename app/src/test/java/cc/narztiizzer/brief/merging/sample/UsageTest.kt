package cc.narztiizzer.brief.merging.sample

import cc.narztiizzer.brief.merging.MergerBuilder
import cc.narztiizzer.brief.merging.interfaces.InstanceMerger
import cc.narztiizzer.brief.merging.sample.model.MergeModel

class UsageTest {
    private fun example(){
        val list = ArrayList<MergeModel>()
        val target = ArrayList<MergeModel>()

        val mergerBuilder = MergerBuilder(MergeModel::class.java).
            setInstanceMerger(object: InstanceMerger<MergeModel> {
                override fun onMergeInstance(item1: MergeModel?, item2: MergeModel?): MergeModel? {
                    return item1
                }
            })

        mergerBuilder.create().mergeBetween(list, target)
    }
}