package xyz.gnarbot.gnar.commands.executors.media

import org.apache.http.client.utils.URIBuilder
import org.jsoup.Jsoup
import xyz.gnarbot.gnar.commands.*
import java.io.IOException

@Command(
        aliases = ["google"],
        usage = "(query...)",
        description = "Who needs browsers!?"
)
@BotInfo(
        id = 50,
        category = Category.MEDIA
)
class GoogleCommand : CommandExecutor() {
    override fun execute(context: Context, label: String, args: Array<String>) {
        if (args.isEmpty()) {
            context.bot.commandDispatcher.sendHelp(context, info)
            return
        }

        try {
            val query = args.joinToString(" ")

            val url = URIBuilder("http://www.google.com/search").addParameter("q", query).build().toString()

            val blocks = Jsoup.connect(url)
                    .userAgent("Gnar")
                    .get()
                    .select(".g")

            if (blocks.isEmpty()) {
                context.send().error("No search results for `$query`.").queue()
                return
            }

            context.send().embed {
                setAuthor("Google Results", "https://www.google.com/", "https://www.google.com/favicon.ico")
                setThumbnail("https://gnarbot.xyz/assets/img/google.png")

                desc {
                    var count = 0

                    buildString {
                        for (block in blocks) {
                            if (count >= 3) break

                            val list = block.select(".r>a")

                            if (list.isEmpty()) continue

                            val entry = list[0]
                            val title = entry.text()
                            val url1 = entry.absUrl("href").replace(")", "\\)")
                            var desc: String? = null

                            val st = block.select(".st")
                            if (!st.isEmpty()) desc = st[0].text()

                            append("**[$title]($url1)**\n").append(desc).append("\n\n")

                            count++
                        }
                    }
                }
            }.action().queue()
        } catch (e: IOException) {
            context.send().error("Caught an exception while trying to Google stuff.").queue()
            e.printStackTrace()
        }
    }
}
