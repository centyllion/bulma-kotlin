@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package bulma.extension

external interface ToastAnimation {
    val `in`: String
    val out: String
}

class BasicToastAnimation(
    override val `in`: String,
    override val out: String
): ToastAnimation

external interface ToastOptions {
    val message: String

    /** Essentially a Bulma's css class. It can be is-primary, is-link, is-info, is-success, is-warning, is-danger, or any other custom class. Default is a whitesmoke background with dark text as shown here. */
    val type: String

    /** Duration of the notification in milliseconds. Default is 2000 milliseconds. */
    val duration: Long

    /** Position where the notification will be shown. The default is top-right, so if you want it to be on the top-left just add top-left to this option. The available options are: top-left, top-center, top-right, center, bottom-left, bottom-center, and bottom-right. */
    val position: String

    /** Whether the notification will have a close button or not. Default is false. */
    val dismissible: Boolean

    /**  Pauses delay when hovering the notification. Default is false. */
    val pauseOnHover: Boolean

    /** Pauses delay when hovering the notification. Default is false. */
    val closeOnClick: Boolean

    /** Dismisses the notification when clicked. Default is true. */
    val opacity: Double

    /** Default is no animations. */
    val animation: ToastAnimation?
}

class BasicToastOptions(
    override val message: String,
    override val type: String = "",
    override val duration: Long = 2000,
    override val position: String = "top-right",
    override val dismissible: Boolean = false,
    override val pauseOnHover: Boolean = false,
    override val closeOnClick: Boolean = true,
    override val opacity: Double = 1.0,
    override val animation: ToastAnimation? = null
): ToastOptions

external interface BulmaToast {
    fun toast(options: ToastOptions = definedExternally)
}

@JsModule("bulma-toast")
@JsNonModule
external val bulmaToast: BulmaToast
