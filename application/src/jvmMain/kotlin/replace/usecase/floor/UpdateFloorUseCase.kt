package replace.usecase.floor

import replace.datastore.FileStorage
import replace.dto.FloorDto
import replace.dto.UpdateFloorDto
import replace.dto.save
import replace.dto.toDto
import replace.model.File
import replace.model.Floor

object UpdateFloorUseCase {
    suspend fun execute(
        updateFloorDto: UpdateFloorDto,
        fileStorage: FileStorage,
    ): FloorDto {

        val floor = Floor.findById(updateFloorDto.id)

        checkNotNull(floor) { "Floor with id ${updateFloorDto.id} not found" }

        val file = updateFloorDto.planFile?.save(fileStorage)?.let { File.findById(it.fileId) }

        floor.name = updateFloorDto.name
        floor.planFile = file

        floor.refresh()

        return floor.toDto()
    }
}
