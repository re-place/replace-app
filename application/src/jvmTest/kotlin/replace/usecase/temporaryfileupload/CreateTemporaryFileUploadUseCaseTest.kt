package replace.usecase.temporaryfileupload

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import replace.datastore.InMemoryFileStorage
import replace.usecase.useDatabase
import java.io.ByteArrayInputStream

class CreateTemporaryFileUploadUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("create a simple temporary file upload") {
                val fileStorage = InMemoryFileStorage()
                useDatabase {
                    checkAll(
                        10,
                        Arb.string(1..100, codepoints = Codepoint.alphanumeric()),
                        Arb.string(1..5, codepoints = Codepoint.alphanumeric()),
                        Arb.list(Arb.byteArray(Arb.int(1_000..100_000), Arb.byte())),
                    ) { fileName, extension, multipart ->
                        val uploadDtos = multipart.map {
                            CreateTemporaryFileUploadUseCase.execute(
                                "$fileName.$extension",
                                ByteArrayInputStream(it),
                                fileStorage,
                            )
                        }

                        uploadDtos.zip(multipart) { dto, part ->
                            dto.name shouldBe fileName
                            dto.extension shouldBe extension
                            fileStorage.exists(dto.path) shouldBe true
                            fileStorage.readFile(dto.path).use {
                                it.readBytes() shouldBe part
                            }
                        }
                    }
                }
            }
        }
    },
)
