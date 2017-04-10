package com.lunivore.montecarluni.engine

class MontecarluniController(
        val fileInputStreamMaker: IMakeACsvInputStream,
        val csvParser: IParseCsvStreams,
        val distributionCalculator: ICalculateWeeklyDistribution) {

    private var  filename: String = ""
    private var distributionChangedHandler : (List<Int>) -> Unit = {}

    constructor() : this(FileInputStreamMaker(), CsvParser(), DistributionCalculator()) {
    }

    fun requestImport() {
        var stream = fileInputStreamMaker.fileAsInputStream(filename)
        var dates = csvParser.parseCompletedDates(stream)
        var results = distributionCalculator.calculateDistribution(dates)
        distributionChangedHandler(results)
    }

    fun  filenameChanged(filename: String?) {
        this.filename = filename ?: "";
    }

    fun setDistributionHandler(handler : (List<Int>) -> Unit) {
        distributionChangedHandler = handler
    }
}

