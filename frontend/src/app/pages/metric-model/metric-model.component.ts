import {Component, Inject, OnInit, signal} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AnalyzeService} from "../../core/services/analyze.service";
import {AggregatedMetrics, AllAnalyst, SingleMetrics, SingleMetricsArray} from "../../core/models/analyze";
import {FormControl} from "@angular/forms";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {MatSelectChange} from "@angular/material/select";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {MusicPlayerComponent} from "../main-page/music-player/music-player.component";
import {Location} from "@angular/common";
export interface It {
  ids: string
}
@Component({
  selector: 'app-metric-model',
  templateUrl: './metric-model.component.html',
  styleUrls: ['./metric-model.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*', minHeight: "*"})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class MetricModelComponent implements OnInit{
constructor( private analyzeService:AnalyzeService, public dialog: MatDialog, private location:Location) {
}
  expandedElement: AggregatedMetrics | null;
  allMetrics: AggregatedMetrics[] =[];
  singleMetrics: SingleMetricsArray[] = [];
  loading = signal(false);
  loading2 = signal(false);
  loading3 = signal(false);
  displayedColumns: string[] = [ 'Файл', 'Дата загрузки', 'Есть нарушение', 'Тескт'];
  expandedDetail: string[] = [ 'expandedDetail',];
  filteredNegotiations: SingleMetricsArray[] = [];
  dataSource:SingleMetricsArray[];
  size = 5;
  pageIndex = 0;
  filterInput: FormControl = new FormControl('');
  ids: string = ''
  ngOnInit(): void {
    // @ts-ignore
    this.ids = this.location.getState().ids;
    console.log('ss', this.ids)
    console.log('idt',this.ids)
    this.getMetricsByIds();
    this.filterInput.valueChanges.subscribe((res)=>{
      this.filteredNegotiations = this.singleMetrics.filter(r => r.negotiation.fileName.includes(res));
      this.size = 10;
      this.paginate(1, this.filteredNegotiations)
    })
  }
  paginate(event: any, data: SingleMetricsArray[]) {
    this.pageIndex=event;
    this.dataSource = data.slice(event * this.size - this.size, event * this.size);
  }
  getSingleMetricsById() {
    this.loading2.set(true)
    this.analyzeService.getNegotiationAnalysis(this.ids).subscribe({
      next: (res) => {
        if(res) {
          this.singleMetrics  = res;
          this.filteredNegotiations =  this.singleMetrics;
          this.dataSource = this.singleMetrics
          this.loading2.set(false)
        }
      },
      error: () => {
        this.loading2.set(false)
      },
      complete: () => {
      }
    });
  }
  getMetricsByIds() {
    this.loading.set(true)
    this.analyzeService.getMetricsByIds(this.ids).subscribe({
      next: (res) => {
        if(res) {
          this.allMetrics = res.aggregatedMetrics.filter(r => r.value >= 0);
          this.loading.set(false);
          this.getSingleMetricsById();
        }
        // this.scroll(this.reftp)
      },
      error: () => {
        this.loading.set(false);
      },
      complete: () => {
      }
    });
  }
  closeDialog() {
    console.log()
  }

  checkEvent(event: MatSelectChange) {
    this.size = event.value;
    this.paginate(1, this.filteredNegotiations)
  }

  checkOne(id: number) {
    
  }

  loadMusic(id: string, negotiationText: string,analysisId: number ) {
    this.dialog.open(MusicPlayerComponent, {
      width      : '80%',
      height     : '80%',
      autoFocus: false,
      hasBackdrop: true,
      disableClose: true,
      data : {
        id: id,
        text: negotiationText,
        analysisId: analysisId
      },
    })
  }
}
