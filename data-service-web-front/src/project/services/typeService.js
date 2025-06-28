/**
 * 类别
 */
import {API_TYPE} from '@/services/api';
import BaseService from '@/services/baseService';


let Service = Object.assign({}, BaseService,{ urlPrefix: API_TYPE});

export default Service;