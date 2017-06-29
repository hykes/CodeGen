package me.hehaiyang.codegen.config.setting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import me.hehaiyang.codegen.model.Database;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
public class DatabasesSetting {

   private List<Database> databases = Lists.newArrayList();

   public Map<String, Database> getDatabaseMap(){
      Map<String, Database> result = Maps.newHashMap();
      databases.forEach( it -> result.put(it.getName(), it));
      return result;
   }

}
