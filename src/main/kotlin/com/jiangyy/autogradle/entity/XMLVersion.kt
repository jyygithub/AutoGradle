package com.jiangyy.autogradle.entity

class XMLVersion(
        var metadata: MetaData
) {
    class MetaData(
            var versioning: Versioning
    ) {
        class Versioning(
                var latest: String
        )
    }
}