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
  TickBoxGridResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/tick-box-grid-response-summary-res';

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
  selector: 'app-edit-form-response-summary-tick-box-grid',
  imports: [ChartComponent],
  templateUrl: './edit-form-response-summary-tick-box-grid.html',
  styleUrl: './edit-form-response-summary-tick-box-grid.scss',
})
export class EditFormResponseSummaryTickBoxGrid
  extends EditFormResponseSummaryComponent<TickBoxGridResponseSummaryRes>
  implements OnInit {

  public chartOptions: Partial<ChartOptions> | null = null;

  ngOnInit(): void {

    const rows = this.responseSummary().responses;

    const series: ApexAxisChartSeries = rows.length
      ? rows[0].responses.map(column => ({
        name: column.column,
        data: rows.map(row =>
          Number(
            row.responses.find(r => r.columnId === column.columnId)?.responseCount ?? 0
          )
        )
      }))
      : [];

    this.chartOptions = {
      series,

      chart: {
        type: 'bar',
        height: 450,
        toolbar: {
          show: true
        }
      },

      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '60%',
          borderRadius: 4,
          borderRadiusApplication: 'end'
        }
      },

      dataLabels: {
        enabled: false
      },

      stroke: {
        show: true,
        width: 1,
        colors: ['transparent']
      },

      fill: {
        opacity: 1
      },

      xaxis: {
        categories: rows.map(r => r.row)
      },

      yaxis: {
        title: {
          text: 'Selections'
        },
        min: 0,
        forceNiceScale: true,
      },

      legend: {
        position: 'top',
        horizontalAlign: 'center'
      },

      tooltip: {
        enabled: false,
        shared: true,
        intersect: false,
        y: {
          formatter: (value: number) =>
            `${value} selection${value === 1 ? '' : 's'}`
        }
      }
    }
  }
}
