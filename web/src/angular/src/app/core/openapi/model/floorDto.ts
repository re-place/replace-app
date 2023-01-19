/**
 * Replace API
 * API for the Replace application
 *
 * The version of the OpenAPI document: 2022.1-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { FileDto } from './fileDto';
import { BookableEntityDto } from './bookableEntityDto';
import { SiteDto } from './siteDto';


export interface FloorDto { 
    id?: string;
    name?: string;
    siteId?: string;
    site?: SiteDto;
    planFileId?: string;
    planFile?: FileDto;
    bookableEntities?: Array<BookableEntityDto>;
}

