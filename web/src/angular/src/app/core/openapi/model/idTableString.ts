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
import { ColumnEntityIDString } from './columnEntityIDString';
import { ForeignKeyConstraint } from './foreignKeyConstraint';
import { ColumnObject } from './columnObject';
import { PrimaryKey } from './primaryKey';
import { ColumnSet } from './columnSet';
import { Index } from './index';


export interface IdTableString { 
    tableName?: string;
    primaryKey?: PrimaryKey;
    id?: ColumnEntityIDString;
    tableNameWithoutSchemeSanitized$exposed_core?: string;
    indices?: Array<Index>;
    ddl?: Array<string>;
    columns?: Array<ColumnObject>;
    autoIncColumn?: ColumnObject;
    foreignKeys?: Array<ForeignKeyConstraint>;
    tableNameWithoutScheme$exposed_core?: string;
    customPKNameDefined$exposed_core?: boolean;
    fields?: Array<object>;
    source?: ColumnSet;
    realFields?: Array<object>;
}
