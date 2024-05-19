export interface MetricsCalculate {
    "aggregatedMetrics": AggregatedMetrics[],
    singleMetrics: SingleMetrics[]
}
export interface AggregatedMetrics {
    "metric": {
        "title": string,
        "formula": string
    },
    "value": number
}
export interface SingleMetrics {
    "negotiation": {
        "id": number,
        "fileName": string, //ФАЙЛ
        "createdAt": string, //дата загрузки
        "fileType": string, // тип
        "fileStorageId": string,
        "isAnalyzed": boolean,
        "upload": {
            "id": number,
            "parentFileName": string,
            "uploadType": string,
            "uploadDateTime": string,
            "isAnalyzed": boolean
        },
        isSelected?: boolean
    },
    "SingleMetricValueDtos": SingleMetricValueDtos[]
    
}
export interface SingleMetricValueDtos {
    "metric": {
        "title": string,
        "formula": string
    },
    "value": number
}
export interface AllAnalyst {
    "id": number,
    "fileName": string,
    "createdAt": string,
    "fileType": string,
    "fileStorageId": string,
    "isAnalyzed": boolean,
    isSelected?: boolean
}
export interface TextIntervals {
    "id": number,
    "violatedText": string,
    "beginIndex": number,
    "endIndex": number,
    "violatedRegulation": string
}
export interface SingleMetricsArray {
    id: number,
    negotiation: {
        "id": number,
        "fileName": string,
        "createdAt":string,
        "fileType": string,
        "fileStorageId": string,
        "isAnalyzed": boolean
    },
    "negotiationText": string,
    "isViolated": boolean;
    violationIntervals: TextIntervals[]
}