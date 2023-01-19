package replace.usecase.floor

import replace.datastore.FileStorage
import replace.dto.CreateFloorDto
import replace.dto.FloorDto
import replace.dto.save
import replace.dto.toDto
import replace.model.File
import replace.model.Floor

object CreateFloorUseCase {
    suspend fun execute(
        createFloorDto: CreateFloorDto,
        fileStorage: FileStorage,
    ): FloorDto {

        val file = createFloorDto.planFile?.save(fileStorage)?.let { File.findById(it.fileId) }

        val insertedFloor = Floor.new {
            name = createFloorDto.name
            planFile = file
        }

        return insertedFloor.toDto()
    }
}
