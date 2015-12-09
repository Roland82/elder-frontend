var symbol = $('#symbol').attr('value');
$(document).ready(function() {

    $.getJSON('http://localhost:8080/datapoints/' + symbol, function(data) {

        // split the data set into ohlc and volume
        var ohlc = [],
            volume = [],
            movingAverage20Day = [],
            movingAverage50Day = [],
            movingAverage100Day = [],
            movingAverage200Day = [],
            forceIndexThirteenDay = [],
            upperBollingerBandTwoDeviation = [],
            lowerBollingerBandTwoDeviation = [],

            macd = [],
            macdSignalLine = [],
            macdHistogram = [],
            dataLength = data.length;

        for (i = 0; i < dataLength; i++) {
            ohlc.push([
                data[i][0], // the date
                data[i][1], // open
                data[i][2], // high
                data[i][3], // low
                data[i][4] // close
            ]);

            volume.push([
                data[i][0], // the date
                data[i][5] // the volume
            ]);

            movingAverage20Day.push([
                data[i][0],
                data[i][6]
            ]);

            movingAverage50Day.push([
                data[i][0],
                data[i][7]
            ]);

            movingAverage100Day.push([
                data[i][0],
                data[i][8]
            ]);

            movingAverage200Day.push([
                data[i][0],
                data[i][9]
            ]);
//
//            forceIndexThirteenDay.push([
//                data[i][0],
//                data[i][6]
//            ]);
//
//            upperBollingerBandTwoDeviation.push([
//                data[i][0],
//                data[i][7]
//            ]);
//
//            lowerBollingerBandTwoDeviation.push([
//                data[i][0],
//                data[i][8]
//            ]);
//
//            macd.push([
//                data[i][0],
//                data[i][9]
//            ]);
//
//            macdSignalLine.push([
//                data[i][0],
//                data[i][10]
//            ]);
//
//            macdHistogram.push([
//                data[i][0],
//                data[i][11]
//            ]);
//

        }

        // set the allowed units for data grouping
        var groupingUnits = [[
            'week', // unit name
            [1]                             // allowed multiples
        ], [
            'month',
            [1, 2, 3, 4, 6]
        ]];

        // create the chart
        $('#container').highcharts('StockChart', {
            rangeSelector: { inputEnabled: $('#container').width() > 480, selected: 1 },

            series: [
                { type: 'candlestick', name: symbol, data: ohlc },
                { name: 'Upper Bollinger Band 2 Deviation', data: upperBollingerBandTwoDeviation, color: '#FF0000' },
                { name: 'Lower Bollinger Band 2 Deviation', data: lowerBollingerBandTwoDeviation, color: '#FF0000' },
                { type: 'column', name: 'Volume', data: volume, yAxis: 1 },
                { name: 'MACD', data: macd, yAxis: 2 },
                { name: 'MACD Signal Line', data: macdSignalLine, yAxis: 2 },
                { name: 'MACD Histogram', type: 'column', data: macdHistogram, yAxis: 2 },
                { name: 'Force (13 Day)', data: forceIndexThirteenDay, yAxis: 3 },
                { name: 'MA (20)', data: movingAverage20Day, color: '#000000' },
                { name: 'MA (50)', data: movingAverage50Day, color: '#234244' },
                { name: 'MA (100)', data: movingAverage100Day, color: '#098763' },
                { name: 'MA (200)', data: movingAverage200Day, color: '#435143' }
            ],

            plotOptions: {
                candlestick: {
                    color: 'red',
                    upColor: 'green'
                }
            },

            yAxis: [{
                labels: {
                    align: 'right',
                    x: -3
                },
                title: {
                    text: 'OHLC'
                },
                height: '35%',
                lineWidth: 2
            },
                {
                    labels: {
                        align: 'right',
                        x: -3
                    },
                    title: {
                        text: 'Volume'
                    },
                    top: '37%',
                    height: '8%',
                    offset: 0,
                    lineWidth: 2
                },
                {
                    labels: {
                        align: 'right',
                        x: -3
                    },
                    title: {
                        text: 'MACD'
                    },
                    top: '47%',
                    height: '13%',
                    offset: 0,
                    lineWidth: 2
                },
                {
                    labels: {
                        align: 'right',
                        x: -3
                    },
                    title: {
                        text: 'Force 13 Day'
                    },
                    top: '62%',
                    height: '20%',
                    offset: 0,
                    lineWidth: 2
                }
            ]
        });
    });

});