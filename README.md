# UK Mortgage demonstrator

This is a very simple browser application to help me and my friends
sink deeper into the unsettling reality of UK mortgage market.

<!--toc:start-->
- [UK Mortgage demonstrator](#uk-mortgage-demonstrator)
  - [Built with](#built-with)
  - [Development](#development)
<!--toc:end-->

[**🎸Live version**](https://indoorvivants.github.io/uk-mortgage-demonstrator/)

## Built with

- [Scala 3](https://docs.scala-lang.org/tour/tour-of-scala.html)
- [Scala.js](https://scala-js.org) - Scala-to-JavaScript compiler
- [Laminar](https://laminar.dev/) - Scala.js-native UI library
- [UI5 Webcomponents](https://github.com/sherpal/LaminarSAPUI5Bindings) bindings
- [Vite](https://vitejs.dev/) for fast iteration and bundling
- [Chart.js](https://www.chartjs.org/) for charts

![2023-07-11 18 47 16](https://github.com/indoorvivants/uk-mortgage-demonstrator/assets/1052965/2bd892be-6075-4ca0-9af7-721848a391e3)


## Development

To build this application, you need to have installed:

1. [NPM](https://www.npmjs.com/)
2. [SBT](https://www.scala-sbt.org/download.html)


In one terminal run:

```
npm install
npm run dev
```

and open http://localhost:5173

In another, run:

```
sbt ~fastLinkJS
```

This way you should have fast feedback when changing code and see the results reflected in the browser.

## Bundling for deployment

Run 

```
npm run build
```