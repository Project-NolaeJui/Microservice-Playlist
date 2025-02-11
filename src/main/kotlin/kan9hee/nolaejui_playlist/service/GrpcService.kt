package kan9hee.nolaejui_playlist.service

import MusicListServerGrpcKt
import kan9hee.nolaejui_playlist.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcService(private val musicRepository:MusicRepository,
                  private val dataService: DataService)
    :MusicListServerGrpcKt.MusicListServerCoroutineImplBase() {

    override suspend fun createDefaultPickupPlaylist(request: Playlist.UserName): Playlist.GrpcResult {
        try {
            dataService.createDefaultUserPlaylist(request.userName)

            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(true)
                    .setResultMessage("${request.userName}의 픽업용 플레이리스트가 생성되었습니다.")
                    .build()
            }
        } catch (e: IllegalArgumentException) {
            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(false)
                    .setResultMessage("${request.userName}의 플레이리스트 처리 중 오류가 발생했습니다. 사유: $e")
                    .build()
            }
        }
    }

    override suspend fun deleteUsersAllPlaylist (request: Playlist.UserName): Playlist.GrpcResult {
        try {
            dataService.deleteUsersAllPlaylist(request.userName)

            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(true)
                    .setResultMessage("${request.userName}의 모든 플레이리스트가 삭제되었습니다.")
                    .build()
            }
        } catch (e: IllegalArgumentException) {
            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(false)
                    .setResultMessage("${request.userName}의 플레이리스트 처리 중 오류가 발생했습니다. 사유: $e")
                    .build()
            }
        }
    }

    override suspend fun changeMusicInfo(request: Playlist.MusicInfo): Playlist.GrpcResult {
        try {
            val music = dataService.findMusicEntity(request.musicId)
            music.musicTitle = request.musicTitle
            music.artist = request.artist
            music.dataType = request.dataType
            music.dataUrl = request.dataUrl
            music.isPlayable = request.isPlayable
            music.uploader = request.uploaderName

            music.musicTagRelations.clear()
            dataService.addMusicAndTagRelation(request.tagsList.toList(),music)

            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(true)
                    .setResultMessage("음원이 수정되었습니다")
                    .build()
            }
        } catch (e: IllegalArgumentException) {
            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(false)
                    .setResultMessage("${request.musicId} 에 해당하는 음원이 없습니다.")
                    .build()
            }
        }
    }

    override suspend fun deleteMusic(request: Playlist.MusicDataId): Playlist.GrpcResult {
        try {
            musicRepository.deleteById(request.id)

            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(true)
                    .setResultMessage("음원이 삭제되었습니다")
                    .build()
            }
        } catch (e: IllegalArgumentException) {
            return withContext(Dispatchers.Default) {
                Playlist.GrpcResult.newBuilder()
                    .setIsSuccess(false)
                    .setResultMessage("${request.id} 에 해당하는 음원이 없습니다.")
                    .build()
            }
        }
    }
}