package example

import be.doeraene.webcomponents.ui5.*
import com.raquo.laminar.api.L.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSImport

import scalajs.js

trait ChartDataset extends js.Any:
  var label: String
  var data: js.Array[Any]

object ChartDataset:
  def apply(label: String, data: js.Array[Any]) =
    js.Dictionary(
      "label" -> label,
      "data"  -> data
    ).asInstanceOf[ChartDataset]

trait ChartData extends js.Any:
  var labels: js.Array[String]
  var datasets: js.Array[ChartDataset]

@JSImport("chart.js/auto", "Chart")
@js.native
class Chart(ctx: dom.Element, config: js.Any) extends js.Any:
  def destroy(): Unit = js.native
  def update(): Unit  = js.native
  val data: ChartData = js.native

case class Settings(
    housePrice: Double,
    deposit: Double,
    durationYears: Int,
    rate: Double
):
  def loanAmount = housePrice - deposit
  def months     = durationYears * 12
  def monthlyPayment =
    val effectiveMonthlyRate = rate / 100 / 12
    val thing                = math.pow(1 + effectiveMonthlyRate, months)
    loanAmount * effectiveMonthlyRate * thing / (thing - 1)
end Settings

def app =
  val housePrice    = Var(400_000.0)
  val deposit       = Var(50_000.0)
  val durationYears = Var(25)
  val rate          = Var(6.0)
  var optChart      = Option.empty[Chart]
  val combined =
    Signal.combine(housePrice, deposit, durationYears, rate).map(Settings.apply)
  val calculation =
    combined.map(_.monthlyPayment)

  inline def poundValue(source: Source[Double]) =
    span(color.maroon, child.text <-- source.toObservable.map(d => f"£$d%.2f"))

  val sliders = div(
    width := "50%",
    h1("Rate: ", child.text <-- rate.signal.map(y => s"$y%")),
    Slider(
      _.min           := 2,
      _.max           := 15.0,
      _.step          := 1,
      _.value         := 6,
      _.showTooltip   := true,
      _.labelInterval := 1,
      _.showTickmarks := true,
      _.events.onInput.map(_.target.value) --> rate
    ),
    h1("House price: ", poundValue(housePrice)),
    Slider(
      _.min           := 100_000,
      _.max           := 1_200_000,
      _.step          := 25_000,
      _.value         := 400_000,
      _.showTooltip   := true,
      _.labelInterval := 10,
      _.showTickmarks := true,
      _.events.onInput.map(_.target.value) --> housePrice
    ),
    h1("Deposit: ", poundValue(deposit)),
    Slider(
      _.min           := 10_000,
      _.max           := 500_000,
      _.step          := 10_000,
      _.value         := 50_000,
      _.showTooltip   := true,
      _.labelInterval := 5,
      _.showTickmarks := true,
      _.events.onInput.map(_.target.value) --> deposit
    ),
    h1(
      "Duration: ",
      span(
        color.olive,
        child.text <-- durationYears.signal.map(y => s"$y years")
      )
    ),
    Slider(
      _.min           := 10,
      _.max           := 35,
      _.step          := 1,
      _.value         := 25,
      _.showTooltip   := true,
      _.labelInterval := 1,
      _.showTickmarks := true,
      _.events.onInput.map(_.target.value.toInt) --> durationYears
    ),
    h1(
      "Monthly payment: ",
      poundValue(calculation)
    )
  )
  val chart = div(
    width := "50%",
    canvasTag( // onMountUnmount callback to bridge the Laminar world and the Chart.js world
      onMountUnmountCallback(
        // on mount, create the `Chart` instance and store it in optChart
        mount = nodeCtx =>
          val domCanvas: dom.HTMLCanvasElement = nodeCtx.thisNode.ref
          val chart = new Chart(domCanvas, js.Dictionary("type" -> "line"))
          optChart = Some(chart)
        ,
        // on unmount, destroy the `Chart` instance
        unmount = thisNode =>
          for chart <- optChart do chart.destroy()
          optChart = None
      ),
      combined --> { settings =>
        val deposits =
          50_000.to(500_000.min(settings.housePrice.toInt), 5_000)
        val monthlyPayments = deposits.map(dep =>
          settings.copy(deposit = dep.toDouble).monthlyPayment
        )
        optChart.foreach { ch =>
          ch.data.labels = js.Array(deposits.map(_.toString)*)
          ch.data.datasets = js.Array(
            ChartDataset(
              "Monthly payments at various deposit amounts",
              js.Array(monthlyPayments*)
            )
          )
          ch.update()
        }
      }
    )
  )
  div(
    h1(
      "The lolno calculator - why you will never afford the mortgage in the UK"
    ),
    width := "100%",
    div(
      display.flex,
      alignItems.center,
      width := "100%",
      sliders,
      chart
    )
  )
end app

@main
def helloWorld(): Unit =
  renderOnDomContentLoaded(
    dom.document.querySelector("#app"),
    app
  )
