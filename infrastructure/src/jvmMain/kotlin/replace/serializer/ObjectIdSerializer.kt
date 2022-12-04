package replace.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.types.ObjectId

object ObjectIdSerializer : KSerializer<ObjectId> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): ObjectId = ObjectId(decoder.decodeString())
  override fun serialize(encoder: Encoder, value: ObjectId) = encoder.encodeString(value.toString())
}
