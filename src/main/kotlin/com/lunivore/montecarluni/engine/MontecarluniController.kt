package com.lunivore.montecarluni.engine

class MontecarluniController(
        val fileInputStreamMaker: IProvideInputStreams,
        val csvParser: ICreateRecordsFromCvs,
        val distributionCalculator: ICalculateWeeklyDistribution) {

    private var  filename: String = ""
    private var distributionChangedHandler : (List<Int>) -> Unit = {}

    constructor() : this(FileInputStreamProvider(), MultiFormatDateRecordCreator(), DistributionCalculator()) {
    }

    fun requestImport() {
        var stream = fileInputStreamMaker.fetch(filename)
        var dates = csvParser.parseResolvedDates(stream)
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

