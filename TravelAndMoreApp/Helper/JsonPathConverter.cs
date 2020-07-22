using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;

namespace AppResponseWebApi.Helper
{
    class JsonPathConverter : JsonConverter
    {
        public override object ReadJson(JsonReader reader, Type objectType,
                                        object existingValue, JsonSerializer serializer)
        {
            JObject jo = JObject.Load(reader);
            var d = new Dictionary<string, object>(jo.ToObject<IDictionary<string, object>>(), StringComparer.CurrentCultureIgnoreCase);
            object targetObj = Activator.CreateInstance(objectType);

            foreach (PropertyInfo prop in objectType.GetProperties()
                                                    .Where(p => p.CanRead && p.CanWrite))
            {
                JsonPropertyAttribute att = prop.GetCustomAttributes(true)
                                                .OfType<JsonPropertyAttribute>()

                                                .FirstOrDefault();

                string jsonPath = (att != null ? att.PropertyName : prop.Name);
                if (serializer.ContractResolver is LinearContractResolver)
                {
                    var nesting = jsonPath.Split(new[] { '.' });
                    if (nesting.Count() > 0)
                    {
                        int lastIndex = GetLastIndex(nesting);
                        jsonPath = nesting[lastIndex];
                    }

                    if (d.ContainsKey(jsonPath))
                    {
                        jsonPath = d.Where(i => i.Key.Equals(jsonPath, StringComparison.OrdinalIgnoreCase)).Select(j => j.Key).FirstOrDefault();
                    }
                }
                else
                {
                    JToken token = jo.SelectToken(jsonPath);

                    if (token != null && token.Type != JTokenType.Null)
                    {
                        object value = token.ToObject(prop.PropertyType, serializer);
                        prop.SetValue(targetObj, value, null);
                    }
                }
            }

            return targetObj;
        }

        public override bool CanConvert(Type objectType)
        {
            // CanConvert is not called when [JsonConverter] attribute is used
            return false;
        }

        /// <inheritdoc />
        public override void WriteJson(JsonWriter writer, object value,
            JsonSerializer serializer)
        {
            var properties = value.GetType().GetRuntimeProperties().Where(p => p.CanRead && p.CanWrite);
            JObject main = new JObject();
            foreach (PropertyInfo prop in properties)
            {
                JsonPropertyAttribute att = prop.GetCustomAttributes(true)
                    .OfType<JsonPropertyAttribute>()
                    .FirstOrDefault();

                string jsonPath = (att != null ? att.PropertyName : prop.Name);
                var nesting = jsonPath.Split(new[] { '.' });
                int lastIndex = GetLastIndex(nesting);
                JObject lastLevel = main;

                object jValue = prop.GetValue(value);
                if (prop.PropertyType.IsArray)
                {
                    if (jValue != null)
                        lastLevel[nesting[lastIndex]] = JArray.FromObject(jValue);
                }
                else
                {
                    if (prop.PropertyType.IsClass && prop.PropertyType != typeof(System.String))
                    {
                        if (jValue != null)
                            lastLevel[nesting[lastIndex]] = JValue.FromObject(jValue);
                    }
                    else
                    {
                        lastLevel[nesting[lastIndex]] = new JValue(jValue);
                    }
                }
            }
            serializer.Serialize(writer, main);
        }

        private static int GetLastIndex(string[] nesting)
        {
            var lastIndex = nesting.Count() - 1;
            if (nesting[lastIndex] == "value")
                lastIndex--;
            return lastIndex;
        }
    }
}
