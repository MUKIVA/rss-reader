package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Cloud
import com.mukiva.data.store.rss.entity.CloudEntity
import javax.inject.Inject

class CloudMapper @Inject constructor() {

    fun mapAsEntity(cloud: Cloud) = CloudEntity(
        id = cloud.id,
        domain = cloud.domain,
        port = cloud.port,
        path = cloud.path,
        registerProcedure = cloud.registerProcedure,
        protocol = cloud.protocol,
    )

    fun mapAsDomain(entity: CloudEntity) = Cloud(
        id = entity.id,
        domain = entity.domain,
        port = entity.port,
        path = entity.path,
        registerProcedure = entity.registerProcedure,
        protocol = entity.protocol,
    )

}