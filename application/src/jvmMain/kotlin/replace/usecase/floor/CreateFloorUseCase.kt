package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.FloorRepository
import replace.datastore.SiteRepository
import replace.datastore.TemporaryFileRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.dto.toModel

object CreateFloorUseCase {
    suspend fun execute(
        floorDto: FloorDto,
        floorRepository: FloorRepository,
        siteRepository: SiteRepository,
        temporaryFileRepository: TemporaryFileRepository,
        fileRepository: FileRepository,
        fileStorage: FileStorage,
    ): FloorDto {
        val siteId = ObjectId(floorDto.siteId)
        val site = siteRepository.findOneById(siteId)
        checkNotNull(site) { "Site with id $siteId not found" }

        val floorDtoWithPlan = SaveFloorPlanFileUseCase.execute(
            floorDto,
            floorRepository,
            temporaryFileRepository,
            fileRepository,
            fileStorage,
        )

        val insertedFloor = floorRepository.insertOne(floorDtoWithPlan.toModel())

        checkNotNull(insertedFloor) { "Could not insert BookableEntity" }

        return insertedFloor.toDto()
    }
}
