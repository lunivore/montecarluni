package com.lunivore.montecarluni.engine

class MontecarluniController(
        val fileInputStreamMaker: IFetchFilesAsInputStreams,
        val csvParser: IParseResolvedDatesFromCvs,
        val distributionCalculator: ICalculateWeeklyDistribution) {

    private var  filename: String = ""
    private var distributionChangedHandler : (List<Int>) -> Unit = {}

    constructor() : this(ResourceOrFilepathFetcher(), MultiFormatDateResolvedParser(), DistributionCalculator()) {
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

