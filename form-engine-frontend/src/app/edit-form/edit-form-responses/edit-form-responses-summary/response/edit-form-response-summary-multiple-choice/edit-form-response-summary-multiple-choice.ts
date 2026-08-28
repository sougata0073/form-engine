import {Component, OnInit} from '@angular/core';
import {
  ApexAnnotations,
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexFill,
  ApexGrid,
  ApexLegend,
  ApexMarkers,
  ApexNonAxisChartSeries,
  ApexPlotOptions,
  ApexResponsive,
  ApexStates,
  ApexStroke,
  ApexTheme,
  ApexTitleSubtitle,
  ApexTooltip,
  ApexXAxis,
  ApexYAxis,
  NgApexchartsModule,
} from 'ng-apexcharts';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  MultipleChoiceResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/multiple-choice-response-summary-res';

type ChartOptions = {
  series?: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart?: ApexChart;
  xaxis?: ApexXAxis;
  yaxis?: ApexYAxis | ApexYAxis[];
  title?: ApexTitleSubtitle;
  subtitle?: ApexTitleSubtitle;
  dataLabels?: ApexDataLabels;
  stroke?: ApexStroke;
  fill?: ApexFill;
  legend?: ApexLegend;
  tooltip?: ApexTooltip;
  markers?: ApexMarkers;
  plotOptions?: ApexPlotOptions;
  responsive?: ApexResponsive[];
  grid?: ApexGrid;
  annotations?: ApexAnnotations;
  states?: ApexStates;
  theme?: ApexTheme;
  colors?: string[];
  labels?: any;
};

@Component({
  selector: 'app-edit-form-response-summary-multiple-choice',
  imports: [NgApexchartsModule],
  templateUrl: './edit-form-response-summary-multiple-choice.html',
  styleUrl: './edit-form-response-summary-multiple-choice.scss',
})
export class EditFormResponseSummaryMultipleChoice extends EditFormResponseSummaryComponent<MultipleChoiceResponseSummaryRes> implements OnInit {

  public chartOptions: Partial<ChartOptions> | null = null

  ngOnInit() {
    this.chartOptions = {
      series: this.responseSummary().responses.map(r => +r.responseCount),
      chart: {
        width: 380,
        type: 'pie',
      },
      labels: this.responseSummary().responses.map(r => r.option),
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
            },
            legend: {
              position: 'bottom',
            },
          },
        },
      ],
    };
  }

}
