package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FloorRepository
import replace.datastore.SiteRepository
import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.dto.toModel

object CreateFloorUseCase {
    suspend fun execute(
        floorDto: FloorDto,
        floorRepository: FloorRepository,
        siteRepository: SiteRepository,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        fileRepository: FileRepository,
        storage: Storage,
    ): FloorDto {
        val siteId = ObjectId(floorDto.siteId)
        val site = siteRepository.findOneById(siteId)
        checkNotNull(site) { "Site with id $siteId not found" }

        val floorDtoWithPlan = SaveFloorPlanFileUserCase.execute(
            floorDto,
            floorRepository,
            temporaryFileUploadRepository,
            fileRepository,
            storage,
        )

        val insertedFloor = floorRepository.insertOne(floorDtoWithPlan.toModel())

        checkNotNull(insertedFloor) { "Could not insert BookableEntity" }

        return insertedFloor.toDto()
    }
}
