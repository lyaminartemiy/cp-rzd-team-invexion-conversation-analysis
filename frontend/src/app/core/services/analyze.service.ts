import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroments/enviroment";
import {AllAnalyst, MetricsCalculate, SingleMetricsArray, TextIntervals} from "../models/analyze";
import {catchError, of} from "rxjs";
import {NotifierService} from "angular-notifier";

@Injectable({
  providedIn: 'root'
})
export class AnalyzeService {

  constructor(private http: HttpClient, private notifierService:NotifierService) { }
  
  getNegotiations() {
    return  this.http.get<AllAnalyst[]>(`${environment.api}/negotiations`).pipe(
        catchError(() => {
              this.notifierService.notify('error', 'Произошла ошибка!');
              return of(null)
            }
        ));
  }

    uploadZipFile(file: File) {
        const data = new FormData();
        data.append('file', file);
        return this.http.post<any>(`${environment.api}/negotiations/zip/upload`,
            data).pipe(
            catchError(() => {
                    this.notifierService.notify('error', 'Произошла ошибка при добавлении файла!');
                    return of(null)
                }
            ));
    }

    uploadOtherFile(file: File) {
        const data = new FormData();
        data.append('file', file);
        return this.http.post<any>(`${environment.api}/negotiations/file/upload`,
            data).pipe(
            catchError(() => {
                    this.notifierService.notify('error', 'Произошла ошибка при добавлении файла!');
                    return of(null)
                }
            ));
    }
    getAllMetrics() {
      return this.http.get<MetricsCalculate>(`${environment.api}/metrics/calculate/all`).pipe(
          catchError(() => {
                  this.notifierService.notify('error', 'Произошла ошибка!');
                  return of(null)
              }
          ));
    }
    
    getMetricsByIds(ids: string) {
        console.log('sas', ids)
      let q = new HttpParams().append('negotiationIds', ids.toString())
        return this.http.get<MetricsCalculate>(`${environment.api}/metrics/calculate`, {params: q}).pipe(
            catchError(() => {
                    this.notifierService.notify('error', 'Произошла ошибка!');
                    return of(null)
                }
            ));
    }
    getNegotiationAnalysis(ids: string) {
        let q = new HttpParams().append('negotiationIds', ids)
        return this.http.get<SingleMetricsArray[]>(`${environment.api}/negotiation-analysis/detailsByIds`, {params: q}).pipe(
            catchError(() => {
                    this.notifierService.notify('error', 'Произошла ошибка!');
                    return of(null)
                }
            ));
    }
    getViolationsByAnalysisId(id: number) {
        let q = new HttpParams().append('analysisId', id)
        return this.http.get<TextIntervals[]>(`${environment.api}/negotiation-analysis/getViolationsByAnalysisId`, {params: q}).pipe(
            catchError(() => {
                    this.notifierService.notify('error', 'Произошла ошибка!');
                    return of(null)
                }
            ));
    }
}
