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
  NgApexchartsModule,
} from 'ng-apexcharts';
import {EditFormResponseSummaryComponent} from '../../../../../type/edit-form-response-summary-component';
import {
  LinearScaleResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/linear-scale-response-summary-res';

export type ChartOptions = {
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
  selector: 'app-edit-form-response-summary-linear-scale',
  imports: [
    ChartComponent, NgApexchartsModule
  ],
  templateUrl: './edit-form-response-summary-linear-scale.html',
  styleUrl: './edit-form-response-summary-linear-scale.scss',
})
export class EditFormResponseSummaryLinearScale extends EditFormResponseSummaryComponent<LinearScaleResponseSummaryRes> implements OnInit {

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
        height: 350,
        type: 'bar',
      },
      plotOptions: {
        bar: {
          borderRadius: 10,
          dataLabels: {
            position: 'top',
          },
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (val) => {
          if (typeof val === 'number') {
            const total = +this.responseSummary().numberOfResponses
            const percent = ((val / total) * 100).toFixed(2)
            return `${val} (${percent})%`
          }
          return val.toString()
        },
        offsetY: -20,
        style: {
          fontSize: '12px',
          colors: ['#304758'],
        },
      },

      xaxis: {
        categories: this.responseSummary().responses.map(r => r.scale),
        position: 'bottom',
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        crosshairs: {
          fill: {
            type: 'gradient',
            gradient: {
              colorFrom: '#D8E3F0',
              colorTo: '#BED1E6',
              stops: [0, 100],
              opacityFrom: 0.4,
              opacityTo: 0.5,
            },
          },
        },
        tooltip: {
          enabled: true,
        },
      },
      yaxis: {
        axisBorder: {
          show: true,
        },
        axisTicks: {
          show: false,
        },
        labels: {
          show: true,
          formatter: (val) => {
            return val.toString()
          },
        },
      },
      title: {
        floating: true,
        offsetY: 330,
        align: 'center',
        style: {
          color: '#444',
        },
      },
    };
  }

}
