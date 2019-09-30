package bulma.extension

class ToastAnimation(
    val `in`: String,
    val out: String
)

class ToastOptions(
    val message: String,
    /** Essentially a Bulma's css class. It can be is-primary, is-link, is-info, is-success, is-warning, is-danger, or any other custom class. Default is a whitesmoke background with dark text as shown here. */
    val type: String = "",
    /** Duration of the notification in milliseconds. Default is 2000 milliseconds. */
    val duration: Long = 2000,
    /** Position where the notification will be shown. The default is top-right, so if you want it to be on the top-left just add top-left to this option. The available options are: top-left, top-center, top-right, center, bottom-left, bottom-center, and bottom-right. */
    val position: String = "top-right",
    /** Whether the notification will have a close button or not. Default is false. */
    val dismissible: Boolean = false,
    /**  Pauses delay when hovering the notification. Default is false. */
    val pauseOnHover: Boolean = false,
    /** Pauses delay when hovering the notification. Default is false. */
    val closeOnClick: Boolean = true,
    /** Dismisses the notification when clicked. Default is true. */
    val opacity: Double = 1.0,
    /** Default is no animations. */
    val animation: ToastAnimation? = null
)

external interface BulmaToast {
    fun toast(options: ToastOptions = definedExternally)
}

lateinit var bulmaToast: BulmaToast
