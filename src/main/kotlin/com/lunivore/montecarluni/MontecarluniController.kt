package com.lunivore.montecarluni

class MontecarluniController {

    private var  filename: String = ""

    private var distributionChangedHandler : (List<Int>) -> Unit = {}

    fun requestImport() {
        var stream = FileInputStreamMaker().fileAsInputStream(filename)
        var dates = CsvParser().parseCompletedDates(stream)
        var results = DistributionCalculator().calculateDistribution(dates)
        distributionChangedHandler(results)
    }

    fun  filenameChanged(filename: String?) {
        this.filename = filename ?: "";
    }

    fun setDistributionHandler(handler : (List<Int>) -> Unit) {
        distributionChangedHandler = handler
    }
}

