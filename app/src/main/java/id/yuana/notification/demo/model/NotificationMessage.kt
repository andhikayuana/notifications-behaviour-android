package id.yuana.notification.demo.model

/**
 * @author Yuana andhikayuana@gmail.com
 * @since Sep, Wed 05 2018 10.27
 **/
data class NotificationMessage(
        val id: Int,
        val title: String,
        val message: String,
        val roomId: Int
)