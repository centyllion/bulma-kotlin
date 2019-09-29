# Bulma-Kotlin

`bulma-kotlin` is a Kotlin JS library that creates and controls [Bulma](https://bulma.io) elements.
It's developed and used at [Centyllion](https://centyllion.com) ([Twitter](https://twitter.com/centyllion)).


**Notes**:
- The project is in its early days it needs maturing. The API is subject to changes especially the package name which is now just `bulma`.
- There is almost no documentation yet.
-  Some elements depends on Bulma extensions like [Toasts](https://github.com/rfoel/bulma-toast) and [Sliders](https://wikiki.github.io/form/slider), it needs to be sorted out 

## What's this ?

[Bulma](https://bulma.io) is a CSS only library that allows you to build beautiful websites. 
`bulma-kotlin` is a Kotlin library for the browser that helps you build web-apps using Bulma with all elements available as classes.
To create an icon just create an `bulma.Icon` and add it to the DOM, then you can change it's properties that are applied.

```kotlin
// creates the icon
val icon = Icon('plus')
// adds it to the DOM
document.body.appendChild(icon.root)
// the change in style is automatically applied
icon.rounded = true
``` 

`bulma-kotlin` allows you to add simple callback on elements like buttons, inputs, etc.. 
Since Bulma elements can be stacked, it's easy to build complex components.
It also provides controllers that links data and Bulma elements. 
Especially a special `MultipleController` that deals with lists.


To get started import `bulma-kotlin` in your Koltin project, if you use gradle, add the repository:

```gradle
maven("https://dl.bintray.com/centyllion/Libraries")
```
and the dependency:
```gradle
implementation("com.centyllion:bulma-kotlin:0.1.3")
```

The Bulma CSS library isn't added by `bulma-kotlin`, a version must be available ([Getting started with Bulma](https://bulma.io/documentation/overview/start/))
and explicit to allow further extensions to be added.

## Few interesting facts

`bulma-kotlin` relies a lot on [delegated properties](https://kotlinlang.org/docs/reference/delegated-properties.html) to handle Bulma element properties. 
It helps to keep the code clear.
The delegated classes can be found in [utils.kt](https://github.com/centyllion/bulma-kotlin/blob/master/src/main/kotlin/bulma/utils.kt).
Implementing a new property is one liner:
```kotlin
// adds a property to handle the class `is-rounded` in the button
var rounded by className(rounded, "is-rounded", root)
```

This method also apply to multiple children of an element. Here is the complete code for a [Box](https://bulma.io/documentation/elements/box):
```kotlin
/** [Box](https://bulma.io/documentation/elements/box) element. */
class Box(vararg body: BulmaElement) : BulmaElement {
    override val root: HTMLElement = document.create.div("box")

    var body by bulmaList(body.toList(), root)
}
```

When the value of a multiple element is changed a simple [diff](https://github.com/centyllion/bulma-kotlin/blob/master/src/main/kotlin/bulma/diff.kt) algorithm is applied to update the DOM.

## Change logs

### 0.1.6

- Adds support for [Switch](https://wikiki.github.io/form/switch/) extension 

### 0.1.5

- Removes reference to InputEvent which isn't supported by Edge.

### 0.1.4

- Bug fix disable Select element.
- Adds TagDelete element ([source](https://bulma.io/documentation/elements/tag/#modifiers)).
- Adds size and add-ons for Tags ([source](https://bulma.io/documentation/elements/tag/#list-of-tags)).
- Adds support for Delete inside a Tag.

### 0.1.3

- Bug fix Left and right icons for Control element.

### Before

First import with most of the elements and options with Bulma version `0.7.5`.

The known missing elements are:
- Tables ()
- Radio groups

