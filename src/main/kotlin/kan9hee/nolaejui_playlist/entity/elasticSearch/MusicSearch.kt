package kan9hee.nolaejui_playlist.entity.elasticSearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDateTime

@Document(indexName = "elasticmusic")
class MusicSearch(@Id
                  @Field(name = "id", type = FieldType.Long)
                    val id: Long,
                  @Field(type = FieldType.Text)
                    val music_title: String,
                  @Field(type = FieldType.Text)
                    val artist:String,
                  @Field(type = FieldType.Text)
                    val tags: List<String>,
                  @Field(type = FieldType.Text)
                    val data_type: String,
                  @Field(type = FieldType.Text)
                    val data_url: String?,
                  @Field(type = FieldType.Boolean)
                    val is_playable: Boolean,
                  @Field(type = FieldType.Text)
                    val uploader: String,
                  @Field(type = FieldType.Date, format = [DateFormat.date_optional_time])
                  val upload_date: LocalDateTime
) {
}