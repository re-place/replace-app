package replace.usecase.floor

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.dto.CreateFloorDto
import replace.dto.FloorDto
import replace.dto.save
import replace.dto.toDto
import replace.model.File
import replace.model.Floor
import replace.model.Sites

object CreateFloorUseCase {
    suspend fun execute(
        createFloorDto: CreateFloorDto,
        fileStorage: FileStorage,
    ): FloorDto {
        return newSuspendedTransaction {
            val file = createFloorDto.planFile?.save(fileStorage)?.let { File.findById(it.fileId) }

            val insertedFloor = Floor.new {
                name = createFloorDto.name
                siteId = EntityID(createFloorDto.siteId, Sites)
                planFile = file
            }

            insertedFloor.toDto()
        }
    }
}
