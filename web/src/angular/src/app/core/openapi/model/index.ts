/**
 * Replace API
 * API for the Replace application
 *
 * The version of the OpenAPI document: 2023.1-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Table } from './table';
import { ColumnObject } from './columnObject';


export interface Index { 
    columns?: Array<ColumnObject>;
    unique?: boolean;
    customName?: string;
    indexType?: string;
    table?: Table;
    indexName?: string;
}
