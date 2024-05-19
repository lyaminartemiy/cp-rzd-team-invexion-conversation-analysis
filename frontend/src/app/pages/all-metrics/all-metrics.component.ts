import {Component, OnInit, signal} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {AnalyzeService} from "../../core/services/analyze.service";
import {AggregatedMetrics} from "../../core/models/analyze";
import {F} from "@angular/cdk/keycodes";

@Component({
  selector: 'app-all-metrics',
  templateUrl: './all-metrics.component.html',
  styleUrls: ['./all-metrics.component.scss']
})
export class AllMetricsComponent implements OnInit{
  constructor(public dialogRef: MatDialogRef<AllMetricsComponent>, private analyzeService:AnalyzeService) {
  }

  ngOnInit(): void {
    this.getAllMetrics();
  }
  allMetrics: AggregatedMetrics[];
  loading = signal(false);

  closeDialog() {
    this.dialogRef.close()
  }
  getAllMetrics() {
    this.loading.set(true)
    this.analyzeService.getAllMetrics().subscribe({
      next: (res) => {
        if(res) this.allMetrics = res.aggregatedMetrics.filter(r => r.value>=0);
        this.loading.set(false)
      },
      error: () => {
        this.loading.set(false)
      },
      complete: () => {
      }
    });
  }
}
