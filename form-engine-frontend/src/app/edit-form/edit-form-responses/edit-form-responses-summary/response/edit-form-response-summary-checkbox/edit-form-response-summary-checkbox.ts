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
  ChartComponent,
} from 'ng-apexcharts';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  CheckboxResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/checkbox-response-summary-res';

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
  selector: 'app-edit-form-response-summary-checkbox',
  imports: [
    ChartComponent
  ],
  templateUrl: './edit-form-response-summary-checkbox.html',
  styleUrl: './edit-form-response-summary-checkbox.scss',
})
export class EditFormResponseSummaryCheckbox extends EditFormResponseSummaryComponent<CheckboxResponseSummaryRes> implements OnInit {

  public chartOptions: Partial<ChartOptions> | null = null

  ngOnInit() {
    this.chartOptions = {
      series: [
        {
          name: 'Count',
          data: this.responseSummary().responses.map(r => +r.responseCount),
        },
      ],
      chart: {
        type: 'bar',
        height: 350,
      },
      plotOptions: {
        bar: {
          borderRadius: 4,
          borderRadiusApplication: 'end',
          horizontal: true,
        },
      },
      dataLabels: {
        enabled: false,
      },
      xaxis: {
        categories: this.responseSummary().responses.map(r => r.option),
      },
    };
  }

}
