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
  MultipleChoiceGridResponseSummaryRes
} from '../../../../../model/edit-form/responses/summary/multiple-choice-grid-response-summary-res';

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
  selector: 'app-edit-form-response-summary-multiple-choice-grid',
  standalone: true,
  imports: [NgApexchartsModule],
  templateUrl: './edit-form-response-summary-multiple-choice-grid.html',
  styleUrl: './edit-form-response-summary-multiple-choice-grid.scss',
})
export class EditFormResponseSummaryMultipleChoiceGrid
  extends EditFormResponseSummaryComponent<MultipleChoiceGridResponseSummaryRes>
  implements OnInit {

  public chartOptions: Partial<ChartOptions> | null = null;

  ngOnInit(): void {

    const summary = this.responseSummary();
    const rows = summary.responses;

    const series: ApexAxisChartSeries = rows.length === 0
      ? []
      : rows[0].responses.map(column => ({
        name: column.column,
        data: rows.map(row => {
          const response = row.responses.find(r => r.columnId === column.columnId);
          return Number(response?.responseCount ?? 0);
        })
      }));

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
        categories: rows.map(r => r.row),
      },

      yaxis: {
        title: {
          text: 'Responses'
        },
        min: 0,
        forceNiceScale: true
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
          formatter: (value: number) => `${value} response${value === 1 ? '' : 's'}`
        }
      }
    }
  }
}
