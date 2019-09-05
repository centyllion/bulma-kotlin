# Bulma-Kotlin

`bulma-kotlin` is a Kotlin JS library that create and control [Bulma](https://bulma.io) elements.

## What's this ?

Bulma is a CSS only library that allows you to build beautiful websites. 
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
It also provides controllers that links data and Bulma elements. 
Especially a special `MultipleController` that deals with lists.


To get started import `bulma-kotlin` in

**Notes**:

- The Bulma CSS library isn't added by `bulma-kotlin`, a version must be available ([Getting started with Bulma](https://bulma.io/documentation/overview/start/))
- `bulma-kotlin` is in its early days it needs maturing. The API is subject to changes especially the package name which is now just `bulma`.

## Change logs

### 0.1.3

- Bug fix disable Select element.
- Adds TagDelete element ([source](https://bulma.io/documentation/elements/tag/#modifiers)).
- Adds size and add-ons for Tags ([source](https://bulma.io/documentation/elements/tag/#list-of-tags)).

### 0.1.2

- Bug fix Left and right icons for Control element.

### 0.1 and 0.1.1

First import with all the elements and almost all the options with Bulma version `0.7.5`.

