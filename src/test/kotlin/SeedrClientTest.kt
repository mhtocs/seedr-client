import io.github.cdimascio.dotenv.dotenv
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class SeedrClientTest {
    private val dotenv = dotenv();
    private val client = SeedrClient();
    private var token: String = dotenv.get(
        "SEEDR_ACCESS_TOKEN",
        client.login(dotenv.get("SEEDR_USER"), dotenv.get("SEEDR_PASS")).access_token
    );


    @Test
    fun addMagnetTest() {
        val magnetURLForBigFile = "magnet:?xt=urn:btih:f1088e99112931184eb288bf8bb7e431b05ddb86&dn=Titans.2018.S03E07.1080p.WEB.H264-EXPLOIT%5Beztv.re%5D.mkv%5Beztv%5D&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A80&tr=udp%3A%2F%2Fglotorrents.pw%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Fexodus.desync.com%3A6969"
        val info = client.addMagnet(magnetURLForBigFile);
        assertNotNull(info);
        assertEquals(200, info?.get("code"))
        assertEquals("not_enough_space_added_to_wishlist", info?.get("result"))


        val magnetURLSmollFile = "magnet:?xt=urn:btih:4e33178c3919248ca18a36dc60ceb2e59e23ff86&dn=What.If.2021.S01E05.What.If.Zombies.720p.DSNP.WEBRip.DDP5.1.x264-FLUX%5Beztv.re%5D.mkv%5Beztv%5D&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A80&tr=udp%3A%2F%2Fglotorrents.pw%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Fexodus.desync.com%3A6969";
        val info2 = client.addMagnet(magnetURLSmollFile);
        assertNotNull(info2);
        assertEquals(200, info2?.get("code"))
        assertEquals(true, info2?.get("result"))
    }

    @Test
    fun listFilesTest() {
        assertEquals(-1, client.listFiles().get("parent"));
    }


    @Test
    fun getFileTest() {
    //    println(client.getFile("950003631"));
    }
}