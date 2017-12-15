package com.github.hykes.codegen.config.setting;

import com.github.hykes.codegen.model.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/10
 */
public class DatabasesSetting {

   private List<Database> databases = new ArrayList<>();

   public Map<String, Database> getDatabaseMap(){
      Map<String, Database> result = new HashMap<>();
      databases.forEach( it -> result.put(it.getName(), it));
      return result;
   }

   public List<Database> getDatabases() {
      return databases;
   }

   public void setDatabases(List<Database> databases) {
      this.databases = databases;
   }
}
