package com.lunivore.montecarluni

import com.google.inject.AbstractModule
import com.lunivore.montecarluni.app.ClipboardCopier
import com.lunivore.montecarluni.engine.DistributionCalculator
import com.lunivore.montecarluni.engine.FileInputStreamProvider
import com.lunivore.montecarluni.engine.Forecaster
import com.lunivore.montecarluni.engine.RecordCreator

class MModule : AbstractModule() {
    override fun configure() {
        val events = Events()
        bind(Events::class.java).toInstance(events)

        var inputProvider = FileInputStreamProvider(events)
        var distributionCalculator = DistributionCalculator(events)
        var recordCreator = RecordCreator(events)
        var clipboardCopier = ClipboardCopier(events)
        var forecaster = Forecaster(events)
    }

}