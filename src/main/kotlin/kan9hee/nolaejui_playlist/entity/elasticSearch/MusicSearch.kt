package kan9hee.nolaejui_playlist.entity.elasticSearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDate

@Document(indexName = "elasticmusic")
class MusicSearch(@Id
                  @Field(name = "id", type = FieldType.Keyword)
                    val id: Long,
                  @Field(type = FieldType.Text)
                    val musicTitle: String,
                  @Field(type = FieldType.Text)
                    val artist:String,
                  @Field(type = FieldType.Keyword)
                    val tags: List<String>,
                  @Field(type = FieldType.Keyword)
                    val dataType: String,
                  @Field(type = FieldType.Text)
                    val dataUrl: String?,
                  @Field(type = FieldType.Boolean)
                    val isPlayable: Boolean,
                  @Field(type = FieldType.Keyword)
                    val uploader: String,
                  @Field(type = FieldType.Date, format = [DateFormat.year_month_day])
                    val uploadDate: LocalDate
) {
}