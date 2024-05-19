import {User} from "./user";

export interface PortfolioDistribution {
  spotDistribution: number,
  futuresDistribution: number
}
export interface Portfolio {
  id: number,
  user?: User,
  name: string
}

export interface Price {
  isSuccess: boolean,
  message: string,
  userPortfoliosTotalPrice: number
}
export interface PriceByPortfolioAndDate {
  dates: string[],
  portfolioPrices: number[]
}
export interface Profit {
  isSuccess: boolean,
  message: string,
  portfolioProfit: number
}
export interface InvestmentPositionsByPortfolio {
  "isSuccess": boolean,
  "message": string,
  "investmentPositions": InvestmentPosition[]
}
export interface GetPortfoliosCountByUserIdResponse {
  "isSuccess": boolean,
  "message": string,
  "portfoliosAmount": number
}
export interface GetTotalPrice {
  "isSuccess": boolean,
  "message": string,
  "userPortfoliosTotalPrice" : number,
}
export interface PositionDistribution {
  "spotDistribution": number,
  "futuresDistribution": number
}
export interface GetProfit {
  "isSuccess": boolean,
  "message": string,
  "portfolioProfit" : number,
}
export interface AddPosition{
  "tickerId": number,
  "portfolioId": number,
  "openDate": string,
  "closeDate": string,
  "openQuoteAssetPrice": number,
  "closeQuoteAssetPrice": number,
  "baseAssetAmount": number
}
export interface InvestmentPosition {
  "id": number,
  "ticker": Ticker,
  "portfolio": {
    "id": number,
    "user": User,
    "name": string
  },
  "openDate": string,
  "closeDate": string,
  "openQuoteAssetPrice": number,
  "closeQuoteAssetPrice": number,
  "baseAssetAmount": number

}
export interface Ticker {
  "id": number,
  "symbol": string,
  "exchangeTickerSymbol": string,
  "exchangeName": string,
  "baseAsset": {
    "id": number,
    "symbol": string,
    "assetTypeName": string
  },
  "quoteAsset": {
    "id": number,
    "symbol": string,
    "assetTypeName": string
  },
  "denominationType": string,
  "expirationType":string,
  "marketType": string,
  "inUse": boolean
}
