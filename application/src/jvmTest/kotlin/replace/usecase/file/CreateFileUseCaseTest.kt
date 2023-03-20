package replace.usecase.file

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.File
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.temporaryFile
import replace.usecase.useDatabase

class CreateFileUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("create a simple file") {
                val fileStorage = InMemoryFileStorage()
                useDatabase {
                    checkAll(
                        20,
                        ReplaceArb.temporaryFile(fileStorage),
                    ) { tempFile ->
                        val fromUseCase = CreateFileUseCase.execute(tempFile.file.id.value, fileStorage)

                        val fromDb = transaction {
                            File.findById(fromUseCase.id)
                        }
                        fromDb shouldNotBe null
                        fromDb!!

                        fromUseCase.id shouldBe fromDb.id.value
                        fromUseCase.name shouldBe fromDb.name
                        fromUseCase.path shouldBe fromDb.path
                        fromUseCase.mime shouldBe fromDb.mime
                        fromUseCase.extension shouldBe fromDb.extension
                        fromUseCase.sizeInBytes shouldBe fromDb.sizeInBytes
                    }
                }
            }
        }
    },
)
