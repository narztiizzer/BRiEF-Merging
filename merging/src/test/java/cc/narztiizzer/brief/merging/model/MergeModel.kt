package cc.narztiizzer.brief.merging.model

import cc.narztiizzer.brief.merging.annotation.Mergable
import cc.narztiizzer.brief.merging.annotation.Unique

@Mergable
class MergeModel {
    @Unique
    var uniqueID = 0L
    var message = ""
}