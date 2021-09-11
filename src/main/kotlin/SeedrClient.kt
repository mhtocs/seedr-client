import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.time.Duration
import java.time.temporal.ChronoUnit

class SeedrClient {
    private val okClient = OkHttpClient.Builder().connectTimeout(Duration.of(10, ChronoUnit.MINUTES)).build();
    private lateinit var token: SeedrToken;
    private val gson = Gson();

    fun login(username: String, password: String): SeedrToken {
        val body: RequestBody = FormBody.Builder()
            .add("grant_type", "password")
            .add("client_id", "seedr_chrome")
            .add("type", "login")
            .add("username", username)
            .add("password", password)
            .build()

        val request: Request = Request.Builder()
            .url("https://www.seedr.cc/oauth_test/token.php")
            .post(body)
            .build()

        okClient.newCall(request).execute().use { response ->
            val bodyString: String = response.body?.string() ?: "";
            this.token = gson.fromJson(bodyString, SeedrToken::class.java)
        }

        return this.token;
    }


    /**
     * Adds the torrent to your seedr account, seedr will fetch and down the torrent in background
     *
     * @param magnetURL                 magnet URL of the torrent to download
     * @return JSONObject               response from seedr
     * @throws IOException              if client fails
     */
    @Throws(IOException::class)
    fun addMagnet(magnetURL: String): JSONObject? {
        val body: RequestBody = FormBody.Builder()
            .add("access_token", token.access_token)
            .add("func", "add_torrent")
            .add("torrent_magnet", magnetURL)
            .build()
        val request: Request = Request.Builder()
            .url("https://www.seedr.cc/oauth_test/resource.php")
            .post(body)
            .build()

        okClient.newCall(request).execute().use { response ->
            val bodyString: String = response.body?.string() ?: ""
            return JSONObject(bodyString)
        }
    }


    /**
     * List folder & files in root directory
     *
     * @return JSONObject
     * @throws IOException              if client fails
     */
    @Throws(IOException::class)
    fun listFiles(): JSONObject {
        val url = "https://www.seedr.cc/api/folder?access_token=${token.access_token}"
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()

        okClient.newCall(request).execute().use { response ->
            val bodyString: String = response.body?.string() ?: "FAIL"
            return JSONObject(bodyString)
        }
    }


    /**
     * List files for a particular folder
     *
     * @param folderID                  ID of the folder whose content to list
     * @return JSONObject
     * @throws IOException              if client fails
     */
    @Throws(IOException::class)
    fun listFiles(folderID: String): JSONObject {
        val url = "https://www.seedr.cc/api/folder/$folderID?access_token=${token.access_token}"
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()

        okClient.newCall(request).execute().use { response ->
            val bodyString: String = response.body?.string() ?: ""
            return JSONObject(bodyString)
        }
    }


    /**
     * Get the file 's url & metadata for the file
     *
     * @param folderFileID              ID of the file to get
     * @return JSONObject
     * @throws IOException              if client fails
     */
    @Throws(IOException::class)
    fun getFile(folderFileID: String): JSONObject {
        val body: RequestBody = FormBody.Builder()
            .add("access_token", token.access_token)
            .add("func", "fetch_file")
            .add("folder_file_id", folderFileID)
            .build()
        val request: Request = Request.Builder()
            .url("https://www.seedr.cc/oauth_test/resource.php")
            .post(body)
            .build()

        okClient.newCall(request).execute().use { response ->
            val bodyString: String = response.body?.string() ?: ""
            return JSONObject(bodyString)
        }
    }


}